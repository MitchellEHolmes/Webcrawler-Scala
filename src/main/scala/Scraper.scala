package scala.crawler

import java.net.URL

import akka.actor.{Actor, ActorRef}
import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.Jsoup

import scala.collection.JavaConverters._

/**
  * @author Mitchell Holmes
  */

class Scraper(indexer: ActorRef) extends Actor {
  val urlValidator = new UrlValidator()

  def receive: Receive = {
    case Scrap(url) =>
      println(s"scraping $url")
      val content = parse(url)
      sender() ! ScrapFinished(url)
      indexer ! Index(url, content)
  }

  def parse(url: URL): Content = {
    val item: GPlayItem = new GPlayItem()
    val linkDomain = "https://play.google.com"
    val link: String = url.toString
    val response = Jsoup.connect(link).ignoreContentType(true)
      .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1").execute()
    
    val contentType: String = response.contentType
    if (contentType.startsWith("text/html")) {
      val doc = response.parse()
      item.title = doc.getElementsByAttributeValue("class", "id-app-title")
        .asScala.map(e => e.text()).head
      val links: List[URL] = doc.getElementsByAttributeValue("class", "card-click-target")
        .asScala.map(e => e.attr("href")).map(link => new URL(linkDomain.concat(link))).toList
      Content(item.title, "putty", links)
    } else {
      // e.g. if this is an image
      Content(link, contentType, List())
    }
  }
}
