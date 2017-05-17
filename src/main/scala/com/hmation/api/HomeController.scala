package com.hmation.api

import javax.inject.Inject

import play.api.mvc.InjectedController


class HomeController @Inject() extends InjectedController {

  def index = Action {
    Ok("API works!")
  }

}
