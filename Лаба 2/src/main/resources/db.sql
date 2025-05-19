--Демографические данные опрошенного
CREATE TABLE demographics
(
    --идентификатор опрошенного
    id UInt32,
    --возраст опрошенного
    age Int8,
    --пол опрошенного (Мужчина, Женщина)
    gender String,
    --профессия опрошенного
    occupation String,
    --место проживания (Город России) опрошенного
    location String,
    --дата регистрации на опрос
    registration_date Date
)
ENGINE MergeTree
ORDER BY id;

--Образ жизни
CREATE TABLE habits
(
    --идентификатор записи о образа жизни 
    id UInt32,
    --идентификатор опрошенного
    user_id UInt32,
    --среднее количество часов сна в сутки (4-10)
    sleep_hours Float32,
    --среднее количество дней в неделю с физической активностью (2-7)
    physical_activity_days Int8,
    --частота социальных взаимодействий (Ежедневно, Еженедельно, Ежемесячно, Редко)
    social_interaction_freq String,
    --Год, к которому относятся данные о образе жизни
    year Int16
)
ENGINE MergeTree
ORDER BY id;

--Опрос о психическом здоровье
CREATE TABLE mental_stats
(
    --идентификатор опроса
    id UInt32,
    --идентификатор опрошенного
    user_id UInt32,
    --уровень стресса (0-10)
    stress_level Int8,
    --уровень тревожности (0-10)
    anxiety_level Int8,
    --уровень депрессии (0-10)
    depression_level Int8,
    --дата проведения опроса
    date Date
)
ENGINE MergeTree
ORDER BY id;

CREATE TABLE demographics_collapsing
(
    id UInt32,
    age Int8,
    gender String,
    occupation String,
    location String,
    registration_date Date,
    Sign Int8
)
ENGINE = CollapsingMergeTree(Sign)
ORDER BY id;

CREATE TABLE demographics_replacing
(
    id UInt32,
    age Int8,
    gender String,
    occupation String,
    location String,
    registration_date Date,
    version UInt32,
    deleted UInt8
)
ENGINE = ReplacingMergeTree(version, deleted)
ORDER BY id;