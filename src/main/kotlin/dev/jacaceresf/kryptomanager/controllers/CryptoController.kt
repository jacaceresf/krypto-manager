package dev.jacaceresf.kryptomanager.controllers

import dev.jacaceresf.kryptomanager.services.CryptoService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cryptos")
class CryptoController(private val cryptoService: CryptoService) {


}