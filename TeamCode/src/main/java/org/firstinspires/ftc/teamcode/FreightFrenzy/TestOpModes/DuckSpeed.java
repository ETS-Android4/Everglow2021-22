/*
Copyright (c) 2018 FIRST

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of FIRST nor the names of its contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode.FreightFrenzy.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FreightFrenzy.Systems.DuckSystem;
import org.firstinspires.ftc.teamcode.FreightFrenzy.Utils.EverglowGamepad;


@Autonomous(name = "Duck Test", group = "Test")
public class DuckSpeed extends LinearOpMode {
    DuckSystem duckSystem;
    EverglowGamepad ourGamepad1;

    @Override
    public void runOpMode() {
        duckSystem = new DuckSystem(this);
        ourGamepad1 = new EverglowGamepad(gamepad1);
        waitForStart();
        while(opModeIsActive()) {
            ourGamepad1.update();
            if (ourGamepad1.rb()) {
                duckSystem.increaseSpeed();
            }
            if (ourGamepad1.lb()) {
                duckSystem.decreaseSpeed();
            }
            if(ourGamepad1.dpad_left()){
                duckSystem.runFor(3000);
            }
            telemetry.addData("CurrentSpeed", duckSystem.GetSpeed());
            telemetry.update();
        }
    }

}
