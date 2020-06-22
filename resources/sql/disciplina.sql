CREATE TABLE disciplina (
codigo serial NOT NULL,
nome varchar(50) NOT NULL,
semestre integer NOT NULL,
curso integer NOT NULL,
PRIMARY KEY (codigo),
FOREIGN KEY (curso) REFERENCES curso (codigo)
);