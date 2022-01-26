# Projet communication-with-quarkus 

Le but de se projet est de tester quarkus en version Native .

Les scenariis suivant sont implémentés : 

* Communication avec une base de données postgresql.
* Chargement d'un fichier sur le serveur et récupération de ce fichier via un volume 

# Lancement du conteneur Docker

Avant de pouvoir lancer le conteneur, il faut remplacer les valeurs `${}`

```shell
docker run -t -p 8080:8080  \
       -e QUARKUS_DATASOURCE_DB_KIND=${quarkus.datasource.db.kind} \
       -e QUARKUS_DATASOURCE_USERNAME=${quarkus.datasource.username} \
       -e QUARKUS_DATASOURCE_PASSWORD=${quarkus.datasource.password} \
       -e QUARKUS_DATASOURCE_REACTIVE_URL=${quarkus.datasource.reactive.url} \ 
       -e QUARKUS_HTTP_BODY_UPLOADS_DIRECTORY=${quarkus.http.body.uploads-directory} \
       -v ${my/directory/to/get/uploaded/file}:${quarkus.http.body.uploads-directory}
       ghcr.io/tmarmillot/communication-with-quarkus
```

par exemple : 

```shell
docker run -t -p 8080:8080  \
       -e QUARKUS_DATASOURCE_DB_KIND=postgresql \
       -e QUARKUS_DATASOURCE_USERNAME=quarkus_test \
       -e QUARKUS_DATASOURCE_PASSWORD=quarkus_test \
       -e QUARKUS_DATASOURCE_REACTIVE_URL=postgresql://localhost:5432/quarkus_test \
       -e QUARKUS_HTTP_BODY_UPLOADS_DIRECTORY=/tmp \
       -v /home/myuser/uploads:/tmp \
       communication-with-quarkus
```

# Scenario 1 : Test de communication avec une base de données postgresql.

Faire la commande suivante : 

```shell
 curl http://${ip-application}:${port-application}/q/health/ready
```

Par exemple : 

```shell
 curl http://localhost:8080/q/health/ready
```

Résultat : 
```json
{
    "status": "UP",
    "checks": [
        {
            "name": "Reactive PostgreSQL connections health check",
            "status": "UP",
            "data": {
                "<default>": "up"
            }
        }
    ]
}
```

# Scenario 2 : Chargement d'un fichier sur le serveur 

 1 - Choisir un fichier  

 2 - Executer la commande curl

```shell
curl -F "file=@absolute/path/to/my/file.txt" http://${ip-application}:${port-application}/test/file -X POST
```

Résultat : 

```text
File uploaded : ${uploaded.file.name}
```

On peut donc retrouver notre fichier dans ${my/directory/to/get/uploaded/file}/${uploaded.file.name} qui correspond au volume docker monté.

Par exemple : 

> Le volume créé en amont pour cette exemple est : -v /Users/Thomas/uploads:/tmp

```shell
curl -F "file=@/Users/Thomas/greetings.txt" http://localhost:8080/test/file -X POST
```

Résultat : 

```text
File uploaded : greetings.txt
```

Le fichiers chargé se situe dans `/Users/Thomas/uploads`