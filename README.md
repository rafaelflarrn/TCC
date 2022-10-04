# TCC
Repositório utilizado para armazenar dados do trabalho de conclusão de curso

## Como rodar o programa

Antes de rodar o programa, é necessário pegar um par de arquivo presente dentro da pasta de samples e
colocar dentro a pasta de input. Os dois arquivos possuem nome de celular.nmea e ublox.nmea. Ambos contém os dados
das coordenadas obtidas por cada equipamento.

Após colocar os arquivos na pasta de input, basta rodar os seguintes comandos na linha de comando.
Você deve estar na pasta que contém o Main.java

```shell

javac Main.java
java Main

```

O primeiro comando serve para compilar e o segundo para de fato rodar.

## Output

O programa gerará um arquivo chamado output.csv na pasta output. Nele há dados ponto a ponto das diferenças entre as
medições escolhidas.