package io.github.koh1o.internshiptrackerapi.repository;

import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

}
