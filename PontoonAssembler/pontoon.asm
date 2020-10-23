; nasm -fmacho64 pontoon.asm
; ld -macosx_version_min 10.15 -lSystem -o pontoon pontoon.o -no_pie

default rel
global _main

section .text

_main:
    ; initialise the random number seed
    call _getTime

    ; show welcome message
    mov rsi, str_Welcome
    mov rdx, str_Welcome.len
    call _print

    ; show opening hand
    mov rsi, str_OpeningHand
    mov rdx, str_OpeningHand.len
    call _print

    ; get a random number and add it to the player's score
    call _random
    call _addRandomToScore
    ; and print it
    mov rax, [rand]
    call _printNumber

    ; print the "and" before the second random number
    mov rsi, str_And
    mov rdx, str_And.len
    call _print

_gameLoop:
    ; get another random number and add it to the player's score
    call _random
    call _addRandomToScore
    ; and print it
    mov rax, [rand]
    call _printNumber

    ; start a new line
    call _newLine

.showCurrentScore:
    ; show current score
    mov rsi, str_CurrentTotal
    mov rdx, str_CurrentTotal.len
    call _print
    mov rax, [val_Score]
    call _printNumber

    ; start a new line
    call _newLine

    ; check if player has gone bust
    mov rax, [val_Score]
    cmp rax, 21
    jg .playerBust

    ; what to do
    mov rsi, str_WhatToDo
    mov rdx, str_WhatToDo.len
    call _print

    ; get user's choice
    call _readOneCharacter

    ; flush input buffer
    ; we need to keep the character we just read
    mov rax, [buf]
    push rax
    call _flushStdin

    ; check if player typed 'd' to draw again
    pop rax
    cmp rax, 'd'
    je _drawNextCard

    ; end of game
    ; show opponent's score
    mov rsi, str_OpponentScored
    mov rdx, str_OpponentScored.len
    call _print
    mov rax, 18
    call _printNumber

    ; start a new line
    call _newLine

    ; check who won
    mov rax, [val_Score]
    cmp rax, 18
    je .draw
    jg .playerWon
    jmp .opponentWon

.draw:
    mov rsi, str_ItsADraw
    mov rdx, str_ItsADraw.len
    call _print
    jmp .exit

.playerWon:
    mov rsi, str_YouWon
    mov rdx, str_YouWon.len
    call _print
    jmp .exit

.opponentWon:
    mov rsi, str_OpponentWon
    mov rdx, str_OpponentWon.len
    call _print
    jmp .exit

.playerBust:
    mov rsi, str_Bust
    mov rdx, str_Bust.len
    call _print
    jmp .exit

.exit:
    mov rax, 0x2000001 ; exit
    mov rdi, 0
    syscall
    ret

_drawNextCard:
    ; get another random number and add it to the player's score
    call _random
    call _addRandomToScore
    ; tell the player what card they just drew
    mov rsi, str_YouDrew
    mov rdx, str_YouDrew.len
    call _print
    mov rax, [rand]
    call _printNumber
    ; start a new line
    call _newLine
    ; return to the game loop
    jmp _gameLoop.showCurrentScore

_readOneCharacter:
    ; read one character from stdin
    mov rax, 0x2000003
    mov rdi, 0
    mov rsi, buf
    mov rdx, 1
    syscall
    ret

_flushStdin:
    call _readOneCharacter
    ; check if we've found the linefeed (ASCII 10)
    mov rax, [buf]
    cmp rax, 10
    ; if we have then we're done
    je .finished
    ; otherwise read the next charater
    jmp _flushStdin
.finished:
    ret

_addRandomToScore:
    ; add the last random value to the player's score
    mov rax, [val_Score]
    add rax, [rand]
    mov [val_Score], rax
    ret

_printNumber:
    ; decimal divisor
    mov r8, 100
    ; will be set to 1 when we find a digit other than 0
    ; this removes leading zeroes
    mov r9, 0
.loop:
    ; divide ax by divisor
    xor rdx, rdx
    div r8
    ; rdx is the remainder, which we store for later
    push rdx
    ; if we've found anything other than 0 then set r9 to 1
    cmp rax, 0
    jne .foundDigit
.checkIfPrinting:
    ; if r9 is 1 then always print
    cmp r9, 1
    je .print
.divideBy10:
    ; divide r8 by 10
    mov rax, r8
    xor rdx, rdx
    mov rcx, 10
    div rcx
    mov r8, rax
    ; retrieve the remainder
    pop rdx
    ; and put it back in rax
    mov rax, rdx
    ; if r8 is greater than 0 then we've still got decimal places to check
    cmp r8, 0
    jg .loop
    ; done
    ret
.foundDigit:
    ; when r9 is set to 1 we know to always print digits from now on
    mov r9, 1
    jmp .checkIfPrinting
.print:
    ; put the ASCII code for 0 in rdx
    mov rdx, '0'
    ; and add it to rax
    add rax, rdx
    ; rax now contans the ASCII code for our random number
    ; store it
    mov [buf], rax
    ; and print it
    mov rsi, buf
    mov rdx, 1
    call _print
    jmp .divideBy10

_newLine:
    ; print a newline character
    mov rsi, str_Newline
    mov rdx, 1
    call _print
    ret

_print:
    ; print string in rsi of length rdx
    mov rax, 0x2000004
    mov rdi, 1
    syscall
    ret

_getTime:
    ; store system time in t_val
    mov rax, 0x2000074
    mov rdi, t_val
    xor rsi, rsi
    syscall
    ret

_random:
    ; remember previous time
    mov rax, [t_val.micro]
    push rax
    ; get new time
    call _getTime
    ; get previous time back
    pop rax
    ; add new to previous to make new seed
    add rax, [t_val.micro]
    ; and save it
    mov [t_val.micro], rax

    ; current seed is still in rax, divide by 10
    xor rdx, rdx
    mov rcx, 10
    div rcx

    ; rdx is the remainder, in the range 0-9
    ; add 1 to put it in the range 1-10
    add rdx, 1

    ; store remainder as random number
    mov [rand], rdx
    ret

section .data

str_Welcome:
db "Welcome to Pontoon!", 10
.len: equ $ - str_Welcome

str_OpeningHand:
db "Your opening hand is: "
.len: equ $ - str_OpeningHand

str_And:
db " and "
.len: equ $ - str_And

str_CurrentTotal:
db "Current total: "
.len: equ $ - str_CurrentTotal

str_WhatToDo:
db "Do you want to draw another card, or stick? (d = draw, s = stick) "
.len: equ $ - str_WhatToDo

str_YouDrew:
db "You drew: "
.len: equ $ - str_YouDrew

str_Bust:
db "You went bust!", 10
.len: equ $ - str_Bust

str_OpponentScored:
db "Your opponent scored: "
.len: equ $ - str_OpponentScored

str_YouWon:
db "You won!", 10
.len: equ $ - str_YouWon

str_OpponentWon:
db "Your opponent won!", 10
.len: equ $ - str_OpponentWon

str_ItsADraw:
db "It's a draw!", 10
.len: equ $ - str_ItsADraw

str_Newline:
db 10

val_Score: dq 0

buf: db 0

t_val:
.seconds dq 0
.micro dw 0

rand: dw 0
