# Análisis Sintáctico y Semántico
Antes hay que definir que es sintáxis y semántica

La sintáxis se define cómo:
>Parte de la gramática que estudia el modo en que se combinan las palabras y los grupos que estas forman para expresar 
> significados, así como las relaciones que se establecen entre todas esas unidades.

Por su parte la semántica
> Estudia diversos aspextos del significado, sentido o interpretacion de signos lingüisticos como simbolos, palabras,
> expresiones o representaciones formales.

Es decir, en esta fase del proyecto, debemos buscar que el código en `assembler` se escriba sin errores.

Para el alcanze del proyecto nos centraremos en las siguientes caracteristicas

## Definicion de segmento de pila
```
stack segment
dw <constante numérica sin signo> dup(<constante numérica>)
ends
``` 
Podemos ver que se compone de 3 partes:
- pseudoinstrucción `stack segment`
- linea de definicion de pilas
  - pseudoinstrucción `dw`
  - constante numerica palabra sin signo
  - pseudoinstrucción `dup()`
    - constante numerica palabra con/sin signo
- pseudoinstrucción que indica la finalización de la definición del stack segment

## Definición del segmento de datos
Es en esta parte dónde se definen variables y constantes, tambien es donde se comienza a llenar la tabla de simbolos.
```asm
data segment
símbolo db <constante caracter>
símbolo db <constante numérica byte con/sin signo>
símbolo db <constante numérica palabra sin signo> dup (<constante caracter byte>)
símbolo db <constante numérica palabra sin signo> dup (<constante numérica byte con/sin signo>)
símbolo dw <constante numérica palabra con/sin signo>
símbolo dw <constante numérica palabra sin signo> dup(<constante numérica palabra con/sin signo>)
símbolo equ <constante numérica palabra con/sin signo>
ends
```
De aquí encontramos que se encuentran 3 partes
- pseudoinstruccion `data segment`
- Lineas de definición de datos
  - cada linea
    1. simbolo
    2. alguna de las siguientes pseudoinstrucciónes `db`, `dw` o `equ`
       - para `db`
         - constante caracter
         - constante numerica byte con/sin signo
         - constante numerica palabra sin signo
           - pseudoinstruccion `dup()`
             - constante caracter byte
             - constante numerica byte con/sin signo
       - para `dw`
         - constante caracter
         - constante numerica palabra con/sin signo
         - constante numerica palabra sin signo
           - pseudoinstruccion `dup()`
             - constante caracter byte
             - constante numerica byte con/sin signo
       - para `equ`
         - constante numerica palabra con/sin signo
- pseudoinstrucción `ends`

## definicion del segmento de código
Aquí nos cerramos a pocas instrucciones, estas se encuentran en [archivo de configuración de instrucciones](src/main/settings/instructions.cfg)
Para analizar estas instrucciones podemos ver que tendremos los siguientes instrucciones

### Sin operandos

### Con un operando
Estas instrucciones podran tener un operando de tipo
- constante
- memoria
- etiqueta
- registro
### Con dos operandos
Estas instrucciones podran tener una de las siguientes
- registro, memoria
- registro, registro
- registro, constante
- memoria, registro
- memoria, constante
- memoria, memoria

# Análisis de la sintáxis

Iniciamos por lo más sencillo, las instrucciones, aprovechado el [archivo de configuración de instrucciones](src/main/settings/instructions.cfg)
podemos utilizar esto para definir ciertas cosas que requerimos para el análisis sintáctico y semántico.
Cada linea de dicho archivo será separada por `,` y tendrá la siguiente estructura
```json
nombre,
sin operando, 
un operando, 
dos operandos, 
constante, 
memoria, 
etiqueta, 
registro, 
registro-memoria, 
registro-registro, 
memoria-registro,
registro-constante
```
Ejemplo para la instruccion `mov`
```json 
mov,0,0,1,0,0,0,0,1,1,1
```