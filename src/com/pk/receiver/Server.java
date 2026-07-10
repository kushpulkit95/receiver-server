/*
 * -----------------------------------------------------------------------------
 * Server Interface
 * -----------------------------------------------------------------------------
 *
 * Purpose:
 * Defines a common contract for every server capable of receiving records.
 *
 * Why use an interface?
 *
 * The receiver supports multiple communication protocols such as:
 *
 *      - TCP
 *      - UDP
 *
 * Although each protocol receives data differently, both servers share the
 * same responsibility: start listening for incoming records.
 *
 * Instead of making ReceiverApplication depend directly on TcpServer or
 * UdpServer, it depends on this interface. This allows the communication
 * protocol to be changed without modifying the application's main workflow.
 *
 * Before introducing this interface, switching protocols required commenting
 * out one server implementation and manually enabling another.
 *
 * With this interface:
 *
 *      protocol = tcp
 *          -> TcpServer.start()
 *
 *      protocol = udp
 *          -> UdpServer.start()
 *
 * ReceiverApplication simply works with a Server reference and calls:
 *
 *      server.start();
 *
 * It does not need to know which protocol is being used.
 *
 * Benefits:
 * - Removes protocol-specific code from ReceiverApplication.
 * - Makes switching protocols easier.
 * - Improves maintainability and future extensibility.
 * - Keeps each class focused on a single responsibility.
 * -----------------------------------------------------------------------------
 */
package com.pk.receiver;

public interface Server {
    void start();
}