CREATE TABLE billing_account (
                                 account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 patient_id VARCHAR(255) NOT NULL,
                                 name VARCHAR(255) NOT NULL,
                                 email VARCHAR(255),
                                 status VARCHAR(50)
);