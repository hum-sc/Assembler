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
Para analizar estas instrucciones podemos ver que tendremos distintos tipos de instrucciones, estos son:
- saltos
- aritmeticas
- transferencia de control
- transferencia de datos
- saltos condicionales
- Instrucciones logicas
- Instrucciones de control de bandera
- Instrucciones de interrupcion

estas a su vez pueden invocarse:

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
- registro, inmediato
- memoria, registro
- memoria, inmediato
- memoria, memoria

## Tamaño de los operandos
- byte
- word

y en combinacion
- byte, byte
- word, byte
- word, word


# Análisis de la sintáxis

Iniciamos por lo más sencillo, las instrucciones, aprovechado el [archivo de configuración de instrucciones](src/main/settings/instructions.cfg)
podemos utilizar esto para definir ciertas cosas que requerimos para el análisis sintáctico y semántico.
Cada linea de dicho archivo será separada por `,` y tendrá la siguiente estructura

0. nombre,
1. sin operando, 
2. un operando, 
3. dos operandos, 
4. inmediate, 
5. memoria, 
6. etiqueta, 
7. registro,
8. regs
9. registro memoria, 
10. registro registro, 
11. registro inmediato
12. memoria registro,
13. memoria inmediato,
14. memoria memoria,
15. regs reg
16. reg regs
17. byte un operando,
18. word un operando,
19. byte byte,
20. word byte,
21. word word,

# Codificación
Aquí se agregan la configuración ára la codificación de las instrucciones, aquí ya no es solo 1's y 0's
22. codigo sin operandos
23. codigo operando inmediato
24. codigo operando memoria
25. codigo operando etiqueta
26. codigo operando registro
27. codigo operando regs
28. Direccionamiento inmmediato
29. direcconamiento memoria
30. direccionamiento etiqueta
31. direccionamiento registro
32. direccionamiento regs
33. desplazamiento inmediato
34. desplazamiento	etiq
35. desplazamiento reg
36. desplazamiento regs
37. inmediato inm
38. inmediato mem
39. inmediato etiq
40. inmediato reg
41. inmediato regs
42. codigo regmem
43. codigo regreg
44. codigo reginm
45. codigo memreg
46. codigo meminm
47. codigo memmem
48. codigo regs,reg
49. codigo reg,regs
50. direccionamiento regmem
51. direccionamiento regreg
52. direccionamiento reginm
53. direccionamiento memreg
54. direccionamiento meminm
55. direccionamiento memmem	
56. direccionamiento regs,reg
57. direccionamiento reg,regs
58. desplazamiento regmem
59. desplazamiento regreg
60. desplazamiento reginm
61. desplazamiento memreg
62. desplazamiento meminm
63. desplazamiento memmem
64. desplazamiento regsreg
65. desplazamiento regregs
66. inmediato regmem
67. inmediato regreg
68. inmediato reginm
69. inmediato memreg
70. inmediato meminm
71. inmediato memmem
72. inmediato regsreg
73. inmediato regregs

