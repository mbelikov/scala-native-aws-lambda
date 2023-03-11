package dev.migapril.aws.lambda.optics

import dev.migapril.aws.lambda.Derive

import java.util.UUID
import monocle.Iso

trait IsUUID[A] {
  def _UUID: Iso[UUID, A]
}

object IsUUID {
  def apply[A: IsUUID]: IsUUID[A] = implicitly

  implicit val identityUUID: IsUUID[UUID] = new IsUUID[UUID] {
    val _UUID = Iso[UUID, UUID](identity)(identity)
  }
}

object uuid extends Derive[IsUUID]
