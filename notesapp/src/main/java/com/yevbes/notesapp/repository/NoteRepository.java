package com.yevbes.notesapp.repository;


import com.yevbes.notesapp.model.Note;
import com.yevbes.notesapp.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    Page<Note> findByTagsIn(List<Tag> tags, Pageable pageable);

    Page<Note> findAllByOrderByCreatedDateDesc(Pageable pageable);
}