package controllers

import helpers.Scraper
import play.api.mvc._


object Application extends Controller {

  def index = Action {
    println(Scraper.getHeroOverallData("soLdier 76"))
    Ok(views.html.index(":D"))
  }
}
