CREATE TABLE respondents_by_location (
    id UUID,
    age INT,
    gender VARCHAR,
    occupation VARCHAR,
    location VARCHAR,
    registration_date DATE,
    PRIMARY KEY ((location), id)
);

CREATE TABLE habits_by_respondent (
    id UUID,
    respondent_id UUID,
    sleep_hours DOUBLE,
    physical_activity_days INT,
    social_interaction_freq VARCHAR,
    survey_date DATE,
    PRIMARY KEY ((respondent_id), id)
);

CREATE TABLE mental_stats_by_respondent (
    id UUID,
    respondent_id UUID,
    stress_level INT,
    anxiety_level INT,
    depression_level INT,
    survey_date DATE,
    PRIMARY KEY ((respondent_id), id)
);

CREATE INDEX IF NOT EXISTS respondents_id_idx
ON respondents_by_location (id);

CREATE INDEX IF NOT EXISTS habits_survey_date_idx
ON habits_by_respondent (survey_date);

CREATE INDEX IF NOT EXISTS mental_stats_survey_date_idx
ON mental_stats_by_respondent (survey_date);

