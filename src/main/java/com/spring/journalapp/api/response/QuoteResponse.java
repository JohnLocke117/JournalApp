package com.spring.journalapp.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteResponse {
    private String status;
    private Data data;

    @Getter
    @Setter
    public class Anime {
        private String name;
    }

    @Getter
    @Setter
    public class Data {
        private String content;
//        private Anime anime;
    }
}






