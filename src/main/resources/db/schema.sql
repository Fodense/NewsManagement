drop table if exists public.comment;
drop table if exists public.news;

CREATE TABLE public.news (
    id_news bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    date timestamp with time zone NOT NULL,
    title text NOT NULL,
    text text NOT NULL,
    PRIMARY KEY (id_news)
);

ALTER TABLE public.news OWNER to postgres;

CREATE TABLE public.comment (
    id_comment bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    date timestamp with time zone NOT NULL,
    text text NOT NULL,
    user_id text NOT NULL,
    news_id bigint NOT NULL,
    PRIMARY KEY (id_comment),
    CONSTRAINT fk_news_comments FOREIGN KEY (news_id)
        REFERENCES public.news (id_news) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
        NOT VALID
);

ALTER TABLE public.comment OWNER to postgres;