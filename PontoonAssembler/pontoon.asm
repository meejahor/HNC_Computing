default rel
global _main

section .text

_main:
    mov rax, 0x2000004 ; write
    mov rdi, 1 ; stdout
    mov rsi, msg
    mov rdx, msg.len
    syscall

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

    call GetTime
    call Random

    mov rax, 0x2000001 ; exit
    mov rdi, [rand]
    syscall

GetTime:
    ; store system time in t_val
    mov rax, 0x2000074
    lea rdi, [t_val]
    xor rsi, rsi
    syscall
    ret

Random:
    ; remember previous time
    mov rax, [t_val.micro]
    push rax
    ; get new time
    call GetTime
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

msg:    db      "Press any key:", 10
.len:   equ     $ - msg
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
