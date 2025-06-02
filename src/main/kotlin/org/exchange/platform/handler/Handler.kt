package org.exchange.platform.handler

import org.exchange.platform.command.Command
import org.exchange.platform.enum.OperationType

interface Handler<C : Command> {
    fun type(): OperationType
    fun handle(command: C): Any?
    fun getCommand(): Class<C>
}