CREATE TABLE usuario (
codigo serial NOT NULL,
matricula varchar(15),
login varchar(50) NOT NULL,
senha varchar(70) NOT NULL,
nome varchar(50) NOT NULL,
perfil char(15) NOT NULL,
email varchar(100) NOT NULL,
telefone varchar(20),
PRIMARY KEY (codigo)
);