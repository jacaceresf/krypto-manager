package dev.jacaceresf.kryptomanager.exceptions

class NotFoundException(override val message: String) : RuntimeException(message) {
}