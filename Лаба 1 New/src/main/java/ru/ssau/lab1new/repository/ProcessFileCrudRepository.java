package ru.ssau.lab1new.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.ssau.lab1new.model.ProcessFile;

public interface ProcessFileCrudRepository extends CrudRepository<ProcessFile, UUID> {} 