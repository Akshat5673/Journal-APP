package com.nineleaps.journalApp.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class WeatherResponse {

    private Location location;
    private Current current;

    @Getter
    @Setter
    public class Current{
        @JsonProperty("observation_time")
        private String observationTime;
        private int temperature;
        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions;
        @JsonProperty("wind_speed")
        private int windSpeed;
        @JsonProperty("wind_degree")
        private int windDegree;
        @JsonProperty("wind_dir")
        private String windDir;
        private int humidity;
        private int cloudcover;
        private int feelslike;

    }

    @Getter
    @Setter
    public class Location{
        private String name;
        private String country;
        private String region;
        @JsonProperty("timezone_id")
        private String timezoneId;
        private String localtime;
        @JsonProperty("utc_offset")
        private String utcOffset;
    }




}
