package ru.ssau.lab1new.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.ssau.lab1new.model.Process;

public interface ProcessCrudRepository extends CrudRepository<Process, UUID>{}
