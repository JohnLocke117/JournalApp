package com.spring.journalapp.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("journalDB")
@Getter
@Setter
public class JournalEntry {
    @Id
    private ObjectId ID;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
}
