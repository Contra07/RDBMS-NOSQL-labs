SELECT table, mutation_id, command, create_time, is_done
FROM system.mutations
order by create_time desc