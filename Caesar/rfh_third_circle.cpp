

// --------------------------------------------------------------------------------------------------
//  Copyright (c) 2016 Microsoft Corporation
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
//  associated documentation files (the "Software"), to deal in the Software without restriction,
//  including without limitation the rights to use, copy, modify, merge, publish, distribute,
//  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in all copies or
//  substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
//  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
//  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// --------------------------------------------------------------------------------------------------

// Malmo:
#include <AgentHost.h>
#include <ClientPool.h>
using namespace malmo;

// STL:
#include <cstdlib>
#include <exception>
#include <iostream>
using namespace std;

#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/json_parser.hpp>

int main(int argc, const char **argv)
{
    AgentHost agent_host;

    try
    {
        agent_host.parseArgs(argc, argv);
    }
    catch (const exception &e)
    {
        cout << "ERROR: " << e.what() << endl;
        cout << agent_host.getUsage() << endl;
        return EXIT_SUCCESS;
    }
    if (agent_host.receivedArgument("help"))
    {
        cout << agent_host.getUsage() << endl;
        return EXIT_SUCCESS;
    }

    ifstream xmlfile{"nb4tf4i_d.xml"};
    string xml{istreambuf_iterator<char>(xmlfile), istreambuf_iterator<char>()};
    MissionSpec my_mission{xml, true};

    MissionRecordSpec my_mission_record("./saved_data.tgz");

    int attempts = 0;
    bool connected = false;
    do
    {
        try
        {
            agent_host.startMission(my_mission, my_mission_record);
            connected = true;
        }
        catch (exception &e)
        {
            cout << "Error starting mission: " << e.what() << endl;
            attempts += 1;
            if (attempts >= 3)
                return EXIT_FAILURE; // Give up after three attempts.
            else
                boost::this_thread::sleep(boost::posix_time::milliseconds(1000)); // Wait a second and try again.
        }
    } while (!connected);

    WorldState world_state;

    cout << "Waiting for the mission to start" << flush;
    do
    {
        cout << "." << flush;
        boost::this_thread::sleep(boost::posix_time::milliseconds(100));
        world_state = agent_host.getWorldState();
        for (boost::shared_ptr<TimestampedString> error : world_state.errors)
            cout << "Error: " << error->text << endl;
    } while (!world_state.has_mission_begun);
    cout << endl;

    // main loop:
    do
    {
        agent_host.sendCommand("move 1");
        boost::this_thread::sleep(boost::posix_time::milliseconds(500));

        for (vector<boost::shared_ptr<TimestampedString>>::iterator it = world_state.observations.begin(); it != world_state.observations.end(); ++it)
        {
            boost::property_tree::ptree pt;
            istringstream iss((*it)->text);
            boost::property_tree::read_json(iss, pt);

            string lookingAt = pt.get<string>("LineOfSight.type");
            string x = pt.get<string>("XPos");

            cout << "x = " << x << endl;
            cout << "<) = " << lookingAt << endl;
        }

        //agent_host.sendCommand("turn 1");
        //boost::this_thread::sleep(boost::posix_time::milliseconds(500));

        world_state = agent_host.getWorldState();

        for (boost::shared_ptr<TimestampedString> error : world_state.errors)
            cout << "Error: " << error->text << endl;
    } while (world_state.is_mission_running);

    cout << "Mission has stopped." << endl;

    return EXIT_SUCCESS;
}
