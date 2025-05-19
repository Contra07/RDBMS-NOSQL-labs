--Средний уровень стресса по городам в каждом году, в котором было более 1 опроса.
SELECT 
    d.location as location,
    AVG(m.stress_level) AS avg_stress,
    toYear(m.date) as year
FROM demographics d
    INNER JOIN mental_stats m ON d.id = m.user_id
GROUP BY d.location, toYear(m.date)
HAVING count(*) > 1
ORDER by location, year