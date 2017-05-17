package com.hmation.http

import javax.inject.Inject

import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter

class Filters @Inject() extends HttpFilters {
  override def filters: Seq[EssentialFilter] = List()
}
