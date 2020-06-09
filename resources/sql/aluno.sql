CREATE TABLE aluno(
matricula serial NOT NULL,
nome varchar(50),
nomemae varchar(50),
nomepai varchar(50),
sexo integer,
logradouro varchar(50),
numero integer,
bairro varchar(40),
cidade varchar(40),
uf character(2),
curso integer,
PRIMARY KEY (matricula),
FOREIGN KEY (curso) REFERENCES curso (codigo)
);