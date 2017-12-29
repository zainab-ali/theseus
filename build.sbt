import xerial.sbt.Sonatype.autoImport.sonatypeProfileName
import ReleaseTransformations._

lazy val compilerSettings = Seq(
  scalacOptions ++= Seq(
    "-language:higherKinds",
    "-Ypartial-unification",
    "-Yliteral-types",
    "-encoding", "UTF-8",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-language:existentials"
  ),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),
  addCompilerPlugin("com.github.mpilquist" %% "simulacrum" % "0.10.0")
)

lazy val commonResolvers = Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.jcenterRepo
)

lazy val buildSettings = Seq(
  scalaOrganization := "org.typelevel",
  scalaVersion := "2.12.0",
  organization := "com.github.to-ithaca",
  name := "theseus"
)

lazy val catsVersion = "0.9.0"
lazy val monocleVersion = "1.4.0"

lazy val commonSettings = Seq(
  resolvers := commonResolvers,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-free" % catsVersion,
    "com.chuusai" %% "shapeless" % "2.3.2",
    "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
    "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
  )
) ++ compilerSettings


val publishSettings = Seq(
  releaseCrossBuild := true,
  releaseIgnoreUntrackedFiles := true,
  sonatypeProfileName := "com.github.to-ithaca",
  developers += Developer("zainab-ali", "Zainab Ali", "", url("http://github.com/zainab-ali")),
  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/to-ithaca/theseus")),
  scmInfo := Some(ScmInfo(url("https://github.com/to-ithaca/theseus"),
    "git@github.com:to-ithaca/theseus")),
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges)
)

lazy val root = (project in file(".")).settings(
  buildSettings,
  commonSettings,
  publishSettings
)
