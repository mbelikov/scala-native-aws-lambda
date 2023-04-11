import Scalac.Keys._

ThisBuild / scalacOptions ++= Seq(
  "-language:_",
  "-Ymacro-annotations",
  "-Wunused:imports", // always on for OrganizeImports
) ++ Seq("-encoding", "UTF-8") ++ warnings.value ++ lint.value

ThisBuild / warnings := {
  if (insideCI.value)
    Seq(
      "-Wconf:cat=lint-byname-implicit:wv,any:error" // for scalac warnings
    )
  else if (lintOn.value)
    Seq(
      "-Wconf:any:warning"
    )
  else
    Seq("-Wconf:any:silent")
}

ThisBuild / lintOn :=
  !sys.env.contains("LINT_OFF")

ThisBuild / lint := {
  if (shouldLint.value)
    Scalac.Lint
  else
    Seq.empty
}

ThisBuild / shouldLint :=
  insideCI.value || lintOn.value
