CREATE TABLE jettysessions (
    sessionid character varying(120) NOT NULL,
    contextpath character varying(60) NOT NULL,
    virtualhost character varying(60) NOT NULL,
    lastnode character varying(60),
    accesstime bigint,
    lastaccesstime bigint,
    createtime bigint,
    cookietime bigint,
    lastsavedtime bigint,
    expirytime bigint,
    maxinterval bigint,
    map bytea
);



CREATE INDEX IF NOT EXISTS idx_jettysessions_expiry
    ON jettysessions USING btree
    (expirytime ASC NULLS LAST)
    TABLESPACE pg_default;
    
CREATE INDEX IF NOT EXISTS idx_jettysessions_session
    ON jettysessions USING btree
    (sessionid COLLATE pg_catalog."default" ASC NULLS LAST, contextpath COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

