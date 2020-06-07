package com.inal.resourceserver.domain.repository;

import com.inal.resourceserver.domain.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {

    List<Notes> findByMember_Id(Long memberId);

}
