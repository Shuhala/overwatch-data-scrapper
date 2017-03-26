package controllers

import play.api.mvc._
import models.OverwatchScraper


object Application extends Controller {

  def index = Action {
    println(OverwatchScraper.getHeroesOverallData.values.foreach(p=> p.foreach(h => println(h))))
    Ok(views.html.index(":D"))
  }

}
