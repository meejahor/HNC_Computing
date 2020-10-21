global _main

section .text

_main:
    mov rax, 0x2000004 ; write
    mov rdi, 1 ; stdout
    mov rsi, msg
    mov rdx, msg.len
    syscall

    mov rax, 0x2000003 ; read
    mov rdi, 0 ; stdin
    mov rsi, buf
    mov rdx, buf.len
    syscall

    mov rax, 0x2000004 ; write
    mov rdi, 1 ; stdout
    mov rsi, buf
    mov rdx, buf.len
    syscall

    mov rax, 0x2000001 ; exit
    mov rdi, 0
    syscall

section .data

msg:    db      "Press any key:", 10
.len:   equ     $ - msg
buf:    db      2 Dup(?)
.len:   equ     $ - buf