CREATE TABLE IF NOT EXISTS public.consulta_examen (
    id_consulta UUID NOT NULL
        REFERENCES public.consulta (id_consulta) ON DELETE CASCADE,
    id_examen UUID NOT NULL
        REFERENCES public.examen (id_examen) ON DELETE RESTRICT,
    PRIMARY KEY (id_consulta, id_examen)
);
