CREATE TABLE IF NOT EXISTS "loans"
(
    "loan_id"            serial PRIMARY KEY,
    "nik"                varchar(16)  NOT NULL,
    "loan_number"        varchar(100) NOT NULL,
    "loan_type"          varchar(100) NOT NULL,
    "total_loan"         int          NOT NULL,
    "amount_paid"        int          NOT NULL,
    "outstanding_amount" int          NOT NULL,
    "created_at"         date         NOT NULL,
    "created_by"         varchar(255) NOT NULL,
    "updated_at"         date         DEFAULT NULL,
    "updated_by"         varchar(255) DEFAULT NULL
);