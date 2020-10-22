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

    call Seed
    ; call Random

    mov rax, 0x2000001 ; exit
    mov rdi, [t_val.micro]
    syscall

Seed:
    mov rax, 0x2000074
    lea rdi, [t_val]
    xor rsi, rsi
    ; lea rsi, [t_zone]
    ; lea rdx, [t_absolute]
    syscall
    ret

; Random:
;     mov rax


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
