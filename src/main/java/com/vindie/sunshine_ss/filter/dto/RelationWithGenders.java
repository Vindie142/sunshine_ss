package com.vindie.sunshine_ss.filter.dto;

import com.vindie.sunshine_ss.common.dto.Gender;
import com.vindie.sunshine_ss.common.dto.Relation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Table(name = "relations_with_genders")
@Data
public class RelationWithGenders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "filter_id", nullable = false)
    private Filter filter;


    @Enumerated(EnumType.ORDINAL)
    @Column(name = "relation", nullable = false)
    private Relation relation;

    @ElementCollection(targetClass = Gender.class, fetch = FetchType.EAGER)
    @JoinTable(name = "relation_with_genders_to_genders", joinColumns = @JoinColumn(name = "relation_with_genders_id"))
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Collection<Gender> genders;
}