package com.spring.journalapp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
    private Location location;
    private Current current;

    @Getter
    @Setter
    public class Location {
        private String name;
        private String region;
        private String country;
    }

    @Getter
    @Setter
    public class Current {
        @JsonProperty("last_updated")
        private String lastUpdated;
        @JsonProperty("temp_c")
        private double tempC;
        @JsonProperty("wind_kph")
        private double windKph;
        @JsonProperty("pressure_mb")
        private double pressureMb;
        @JsonProperty("precip_mm")
        private double precipMM;
        private int humidity;
        private int cloud;
        @JsonProperty("vis_km")
        private double visKM;
        private double uv;
    }
}
