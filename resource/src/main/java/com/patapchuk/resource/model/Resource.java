package com.patapchuk.resource.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Resource {
    @Id
    @SequenceGenerator(
            name = "resource_id_sequence",
            sequenceName = "resource_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "resource_id_sequence"
    )
    private Long id;
    private Long contentLength;
    private String status;
    private String fileName;
}
