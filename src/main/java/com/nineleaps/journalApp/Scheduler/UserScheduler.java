package com.nineleaps.journalApp.Scheduler;

import com.nineleaps.journalApp.Cache.AppCache;
import com.nineleaps.journalApp.Dao.UserRepositoryDao;
import com.nineleaps.journalApp.Entities.JournalEntry;
import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Enums.Sentiment;
import com.nineleaps.journalApp.Services.Impls.EmailService;
import com.nineleaps.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserScheduler {


    private final UserRepositoryDao dao;

    private final AppCache appCache;

    private final KafkaTemplate<String, SentimentData> kafkaTemplate;

    private final EmailService emailService;

    @Autowired
    public UserScheduler(UserRepositoryDao dao,
                         AppCache appCache,
                         KafkaTemplate<String, SentimentData> kafkaTemplate,
                         EmailService emailService) {
        this.dao = dao;
        this.appCache = appCache;
        this.kafkaTemplate = kafkaTemplate;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 10 * * SUN")
    public void fetchUsersAndSendMail(){
        List<User> users = dao.getUsersForSentimentAnalysis();
        for (User user: users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x->x.getDate()
                    .isAfter(LocalDateTime.now().minusDays(7))).map(JournalEntry::getSentiment).toList();
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for(Sentiment sentiment : sentiments){
                if(!ObjectUtils.isEmpty(sentiment))
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;

            for(Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()){
                if (entry.getValue()>maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if(!ObjectUtils.isEmpty(mostFrequentSentiment)) {
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail())
                        .sentiment("Sentiment for last 7 days "+mostFrequentSentiment)
                        .build();
//                try{
//                    kafkaTemplate.send("weekly-sentiments",sentimentData.getEmail(),sentimentData);
//                }catch (Exception e){
                    emailService.sendEmail(sentimentData.getEmail(),"Sentiment for previous week : ",
                            sentimentData.getSentiment());
//                }

            }
        }
    }

    @Scheduled(cron = "0 0/10 * * * * ")
    public void clearAppCache(){
        appCache.init();
    }
}
