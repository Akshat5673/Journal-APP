package com.nineleaps.journalApp.model;


import lombok.*;


@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SentimentData {

    private String email;
    private String sentiment;
}
