package com.whale.enpoint;

/**
 * Core abstraction: event handling mechanism
 */
public interface ActorEnpoint {
  /**
   * Receiving behavior as an individual instance
   * in a distributed environment or abstract behavior
   */
  Object receive();

  /**
   * The behavior of individual instances to send data
   */
  Object send();
}
