# trianafy-base
Repositorio base para el proyecto Trianafy de 2º DAM - Salesianos Triana

Proyecto realizado por Antonio Jiménez Infante.

>## Entidades
> Las entidades que he usado son:
> - Artist
> - Song
> - Playlist
> - GetArtistDTO: {id: artistId, artist: artist name}
> - CreateSongDTO: {“title”: “The song”, “artistId”: 1, “album” : “The album”, “year”: 2000}
> - GetSongDTO: { “id”: 1, “title”: “The song”, “artist”: “Artist name”, “album” : “The album”,
    “year”: 2000}
> - GetSongWithArtistDTO: {
    “id”: 1,
    “title”: “The song”,
    “artist”: { “id”: 1, “artist”: “Artist name” }
    “album” : “The album”,
    “year”: 2000
    }
> - CreatePlaylistDTO: { “id”: 1, “name”: “The name”, “description”: “The desc” }
> - NumberOfSongsDTO: { “id”: 1, “name”: “The name”, “numberOfSongs”: 7 }
> - GetPlaylistDTO: {
    “id”: 1,
    “name”: “The name”,
    “description”: “The desc”
    “songs”: 
    { “id”: 1, “title”: “The song”,
    “artist”: “Artist name”,
    “album” : “The album”, “year”: 2000},
    { “id”: 2, “title”: “Another song”,
    “artist”: “Another Artist name”,
    “album” : “Another album”, “year”: 2020},
    ...
    }

>## Funcionalidad
> Las funcionalidades de esta API son:
> - Crear, editar, borrar, consultar artistas y consultar todos los artistas.
> - Crear, editar, borrar, consultar canciones y consultar todas las canciones.
> - Crear, editar, borrar, consultar playlists y consultar todas las playlists.
> - Añadir, borrar y mostrar todas las canciones o una canción dentro de una playlist.
> 

>## Enlaces de interés
> Algunos enlaces de interés:
> - http://localhost:8080/api-docs.yaml 
> - http://localhost:8080/swagger-ui.html