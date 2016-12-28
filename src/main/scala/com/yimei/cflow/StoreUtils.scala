//import akka.http.scaladsl.model.{ContentTypes, Multipart}
//import org.jboss.netty.handler.codec.http.multipart.FileUpload
//import org.scalatest.{FlatSpec, Matchers}
//
//class StoreUtils extends FlatSpec with Matchers with FileUpload {
//  override def testConfigSource = "akka.loglevel = WARNING"
//
//  import java.io.File
//  "File upload" should "not be able to upload file when file does not exist" in {
//    val file = new File("")
//    val formData = Multipart.FormData.fromFile("file", ContentTypes.`application/octet-stream`, file, 100000)
//    Post(s"/user/upload/file", formData) ~> routes ~> check {
//      status shouldBe StatusCodes.InternalServerError
//      responseAs[String] shouldBe "Error in file uploading"
//    }
//  }
//
//  it should "be able to upload file" in {
//    val file = new File(getClass.getResource("/testFile").toString)
//    val formData = Multipart.FormData.fromFile("file", ContentTypes.`application/octet-stream`, file, 100000)
//    Post(s"/user/upload/file", formData) ~> routes ~> check {
//      status shouldBe StatusCodes.OK
//      responseAs[String] contains "File successfully uploaded"
//    }
//  }
//}
