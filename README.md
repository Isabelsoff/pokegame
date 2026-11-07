# pokegame

App Android para el ámbito competitivo de Pokémon. Permite consultar datos de los Pokémon, armar un equipo de hasta 6, analizar sus debilidades y fortalezas según la tabla de tipos, y guardar el equipo en la nube con una cuenta de usuario.

## Funciones

- Lista de Pokémon con sprites y carga por páginas al hacer scroll.
- Detalle de cada Pokémon: imagen, tipos, altura, peso y estadísticas base.
- Armado de equipo de hasta 6 Pokémon, sin repetidos.
- Análisis del equipo con la tabla de efectividad de los 18 tipos: debilidades a cubrir, tipos sin cobertura y tabla completa de débiles, resistentes e inmunes.
- Registro e inicio de sesión con correo y contraseña.
- El equipo de cada usuario se guarda en la nube y se carga al iniciar sesión.

## Tecnologías

- **Lenguaje:** Kotlin
- **Interfaz:** Jetpack Compose y Navigation Compose
- **Arquitectura:** MVVM (Model, View, ViewModel)
- **Red:** Retrofit2, Gson y corrutinas, consumiendo la [PokéAPI](https://pokeapi.co/)
- **Imágenes:** Coil
- **Usuarios:** Firebase Authentication
- **Nube:** Cloud Firestore

## Estructura del proyecto

```
com.example.pokegame
├── data
│   ├── model        modelos de la API y del equipo
│   ├── remote       Retrofit (PokeApiService, RetrofitClient)
│   └── repository   acceso a datos (PokéAPI, Auth, Firestore)
├── domain           tabla de tipos y análisis del equipo
└── ui
    ├── analysis     pantalla de análisis del equipo
    ├── auth         login y registro
    ├── navigation   rutas de la app
    ├── pokemondetail
    ├── pokemonlist
    ├── team         equipo de hasta 6 Pokémon
    └── theme
```

