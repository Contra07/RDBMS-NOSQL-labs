INSERT INTO demographics_collapsing (*)
SELECT 
    1 as id, 34 as age, 
    'Male' as gender, 
    'Software Engineer' as location, 
    'San Francisco' as occupation, 
    '2019-03-15' as registration_date, 
    1 as Sign, 
UNION ALL
SELECT 
    dc.id as id, 
    null as age,
    null as gender,
    null as location,
    null as occupation,
    null as registration_date,
    -1 as Sign,
from demographics_collapsing as dc
where id = 1
GROUP BY id
HAVING sum(dc.Sign) > 0

INSERT INTO demographics_collapsing (*)
SELECT 
    dc.id as id, 
    null as age,
    null as gender,
    null as location,
    null as occupation,
    null as registration_date,
    -1 as Sign,
from demographics_collapsing as dc
where id = 1
GROUP BY id
HAVING sum(dc.Sign) > 0
UNION ALL
SELECT 
    1 as id, 34 as age, 
    'Male' as gender, 
    'Software Engineer' as location, 
    'San Francisco' as occupation, 
    '2019-03-15' as registration_date, 
    1 as Sign, 