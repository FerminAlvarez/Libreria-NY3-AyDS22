# NY3-New-York-Times-Data
![App Screenshot](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU)

## Descripción del servicio
Librería que permite comunicarse con la API de New York Times

**getArtistInfo(name: String)**

Dado un nombre de artista, getArtistInfo devolverá la información consultada a la New York Times API con un formato:

- artistName: String - *Nombre del artista buscado*
- artistInfo: String - *Descripcion del articulo obtenido mediante la API*
- artistURL: String - *Enlace del articulo obtenido mediante la API*

---
## Referencia al servicio desde el proyecto

Paso 1: Agregar el submodulo al proyecto

```git
git submodule add https://github.com/FerminAlvarez/NY3-New-York-Times-Data libs/NewYorkTimesData
```

Paso 2: Agregar a settings.gradle
```kotlin
include ':newyorkdata'
project(':newyorkdata').projectDir = new File('libs/NewYorkTimesData')
```

Paso 3: Sincronizar gradle en el proyecto

Paso 4: Agregar en el proyecto la dependencia de gradle en build.gradle
```kotlin
implementation project(":newyorkdata")
```

Paso 5: Sincronizar gradle en el proyecto

--- 
## Uso del servicio

Paso 1: Importar el servicio
```kotlin
import ayds.ny3.newyorktimes.NytArticleService
```

Paso 2: Obtener una instancia del servicio
```kotlin
val nytArticleService: NytArticleService = NytInjector.nytArticleService
```

Ejemplo de uso:
```kotlin
val serviceNytArtistInfo = nytArticleService.getArtistInfo(artist)
```

--- 
## Edge cases

En caso de que no haya conexión a internet, getArtistInfo lanzará una excepción de tipo Exception con el mensaje "No Response from api"

En caso de que no se haya encontrado una descripción devolverá un objeto null
