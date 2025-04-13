package ru.ssau.lab1.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.ssau.lab1.model.Process;

public interface ProcessRepository extends CrudRepository<Process, UUID>{}
