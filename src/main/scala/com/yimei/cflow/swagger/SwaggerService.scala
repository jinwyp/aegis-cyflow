package com.yimei.cflow.swagger

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives


// @Api           :
// @Path          :
//
// @ApiOperatioin :
//     - value       :
//     - notes
//     - nickname
//     - httpMethod
//
// @ApiResponses  :
//      - ApiResponse
//            - code
//            - message
//            - response
// @ApiImplicitParams
//      - ApiImplicitParam :
//            - new
//            - value
//            - required
//            - dataType
//            - paramType
//
class SwaggerService extends Directives with DefaultJsonFormats {
  val route = assets

  def assets = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~
      pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }
}

