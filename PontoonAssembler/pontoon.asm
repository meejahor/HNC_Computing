default rel
global _main

section .text

_main:
    mov rsi, str_Welcome
    mov rdx, str_Welcome.len
    call _print

    ; mov rax, 0x2000003 ; read
    ; mov rdi, 0 ; stdin
    ; mov rsi, buf
    ; mov rdx, buf.len
    ; syscall

    ; mov rax, 0x2000004 ; write
    ; mov rdi, 1 ; stdout
    ; mov rsi, buf
    ; mov rdx, buf.len
    ; syscall

    ; initialise the random number seed
    call _getTime

    mov rsi, str_OpeningHand
    mov rdx, str_OpeningHand.len
    call _print

    ; get a random number
    call _random
    ; and print it
    mov rax, [rand]
    call _printValue

    ; print the "and" before the second random number
    mov rsi, str_And
    mov rdx, str_And.len
    call _print

    ; get another random number
    call _random
    ; and print it
    mov rax, [rand]
    call _printValue

    call _newLine

    ; print the "and" before the second random number
    mov rsi, str_Total
    mov rdx, str_Total.len
    call _print

    mov rax, 0x2000001 ; exit
    mov rdi, [rand]
    syscall
    ret

_newLine:
    mov rsi, str_Newline
    mov rdx, 1
    call _print
    ret

_print:
    mov rax, 0x2000004
    mov rdi, 1
    syscall
    ret

_getTime:
    ; store system time in t_val
    mov rax, 0x2000074
    lea rdi, [t_val]
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

_printValue:
    ; if value is less than 10 then go and do a straight ASCII conversion
    cmp rax, 10
    jl _printValueLessThan10
    ; otherwise print '10'
    mov rsi, str_Ten
    mov rdx, 2
    call _print
    ret
_printValueLessThan10:
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
    ret

section .data

str_Welcome:            db "Welcome to Pontoon!", 10
.len: equ               $ - str_Welcome

str_OpeningHand:        db "Your opening hand is: "
.len: equ               $ - str_OpeningHand

str_And:                db " and "
.len: equ               $ - str_And

str_Total:                db "Total: "
.len: equ               $ - str_Total

str_Ten:                db "10"
str_Newline:            db 10

val_Score: dw 0

buf:    db      2
.len:   equ     $ - buf

; struc t_val
;     seconds: resw 1
; endstruc

t_val:
    .seconds dq 0
    .micro dw 0
t_zone: times 2 dq 0
t_absolute: dq 0
rand: dw 0

; struct:
;     istruc t_val
;         at seconds, dw 0
;     iend

; t_val:
;     seconds:    qword   0
;     micro:      dword   0
