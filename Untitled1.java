#include <iostream>
#include <stdlib.h>

#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
#include<math.h>
#include "imageloader.h"

using namespace std;

GLuint _textureIdbrick;
GLuint _textureIdgrass;
GLuint _textureIdsky;
GLuint _textureIdnsky;

int ghurbe=1;
int flag=0;
int night=0;
void textureonoff(unsigned char key, int x, int y) {

    if (key=='l' && flag==0){

             glEnable(GL_LIGHTING);

              flag=1;
              ghurbe=1;
	        }
	        else if(key=='l' && flag==1)
            {
                  glDisable(GL_LIGHTING);
                 flag=0;
                 ghurbe=0;
            }

else if(key=='d' && night==0)
{

    night=1;
}
    else if(key=='n' && night==1)
{

    night=0;
}




}
//loadTexture function takes an Image object and returns a GLuint (which is kind of like an unsigned int) giving the id that OpenGL assigned to the texture.
//Makes the image into a texture, and returns the id of the texture
GLuint loadTexture(Image* image) {
	GLuint textureId; //identify texture

	//The first argument is the number of textures we need, and the second is an array where OpenGL will store the id's of the textures.
	glGenTextures(1, &textureId); //Make room for our texture //1-- koyta texture , &textureID --- memory te room banacchi
	// to assign the texture id to our image data.
//	We call glBindTexture(GL_TEXTURE_2D, textureId) to let OpenGL know
// that we want to work with the texture we just created. Then, we call glTexImage2D to load the image into OpenGL.
	glBindTexture(GL_TEXTURE_2D, textureId); //Tell OpenGL which texture to edit // textureID ke GL diye convert krbo
	//Map the image to the texture
	glTexImage2D(GL_TEXTURE_2D,                //Always GL_TEXTURE_2D
				 0,                            //0 for now
				 GL_RGB,                       //Format OpenGL uses for image //color er jonno
				 image->width, image->height,  //Width and height
				 0,                            //The border of the image //left most corner e pixel dhori eta mean kore
				 GL_RGB, //GL_RGB, because pixels are stored in RGB format
				 GL_UNSIGNED_BYTE, //GL_UNSIGNED_BYTE, because pixels are stored //data type of RGB
				                   //as unsigned numbers
				 image->pixels);               //The actual pixel data
	return textureId; //Returns the id of the texture
}

//The id of the texture

void initRendering() {
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_NORMALIZE);
	glEnable(GL_COLOR_MATERIAL);



	Image* image = loadBMP("brick.bmp");

	//Once we've got the image, we have to send it to OpenGL. We do this in a function we write called loadTexture.
	_textureIdbrick = loadTexture(image);
	//Note that we should only use images whose widths and heights are 64, 128, or 256, since computers like powers of 2.
//	Other sizes of images might not work properly.
	delete image;


	image = loadBMP("grass.bmp");
	_textureIdgrass = loadTexture(image);
	delete image;


	image = loadBMP("daysky.bmp");
	_textureIdsky = loadTexture(image);
	delete image;

	image = loadBMP("nightsky.bmp");
	_textureIdnsky = loadTexture(image);
	delete image;



}

void handleResize(int w, int h) {
	glViewport(0, 0, w, h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(45.0, (float)w / (float)h, 1.0, 200.0);
}
float _angle = -20.0f;
void drawScene() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

	glTranslatef(1.0f, -0.2f, -12.0f);
    GLfloat ambientColor[] = {0.2f, 0.2f, 0.2f, 1.0f}; //Color (0.2, 0.2, 0.2) and intensity //can be greater than 1 so not like color
	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientColor);
    //glEnable(GL_LIGHTING);
	//Add positioned light
	GLfloat lightColor0[] = {0.5f, 0.5f, 0.5f, 1.0f}; //Color (0.5, 0.5, 0.5)
	GLfloat lightPos0[] = {4.0f, 0.0f, 8.0f, 1.0f}; //Positioned at (4, 0, 8)
	glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColor0);
	glLightfv(GL_LIGHT0, GL_POSITION, lightPos0);


	glEnable(GL_TEXTURE_2D);
        if (night == 1) {
            glBindTexture(GL_TEXTURE_2D, _textureIdnsky);
        } else {
            glBindTexture(GL_TEXTURE_2D, _textureIdsky);

        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    glPushMatrix();
	glBegin(GL_QUADS);
    glColor3f(1,1,1);
    glNormal3f(0.0, 0.0f, 1.0f);

    glTexCoord2f(0.0f, 0.0f);
    glVertex3f(-22.0f, -1.95f, -11.0f);
    glTexCoord2f(1.0f, 0.0f);
	glVertex3f(22.0f, -1.95f, -11.0f);
	glTexCoord2f(1.0f, 1.0f);
	glVertex3f(22.0f, 9.95f, -11.0f);
	glTexCoord2f(0.0f, 1.0f);
    glVertex3f(-22.0f, 9.95f, -11.0f);


glEnd();
glPopMatrix();
//(GL_TEXTURE_2D);


glEnable(GL_TEXTURE_2D);

    glBindTexture(GL_TEXTURE_2D, _textureIdgrass);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	glPushMatrix();
	glBegin(GL_QUADS);
    glColor3f(1,1,1);
    glNormal3f(0.0, 1.0f, 0.0f);

    glTexCoord2f(0.0f, 0.0f);
    glVertex3f(-7.0f, -1.95f, 11.0f);
    glTexCoord2f(5.0f, 0.0f);
	glVertex3f(7.0f, -1.95f, 11.0f);
	glTexCoord2f(5.0f, 5.0f);
	glVertex3f(22.0f, -1.95f, -11.0f);
	glTexCoord2f(0.0f, 5.0f);
    glVertex3f(-22.0f, -1.95f, -11.0f);


glEnd();
glPopMatrix();

glDisable(GL_TEXTURE_2D);
 glRotatef(_angle, 0.0f, 1.0f,  0.0f);

	glTranslatef(0.0f,-1.05f,-2.0f);
	glColor3f(1.0f,1.0f,1.0f);
	glBegin(GL_QUADS);

    glColor3f(1,1,1);



    // 2nd left

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(-.20f, 1.10f, 1.0f);
	glVertex3f(-.10f, 1.10f, 1.0f);
	glVertex3f(-.10f, -1.10f, 1.0f);
    glVertex3f(-.20f, -1.10f, 1.0f);

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(-.20f, 1.10f, 0.9f);
	glVertex3f(-.10f, 1.10f, 0.9f);
	glVertex3f(-.10f, -1.10f, 0.9f);
    glVertex3f(-.20f, -1.10f, 0.9f);


    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(-.10f, 1.10f, 0.9f);
    glVertex3f(-.10f, 1.10f, 1.0f);
    glVertex3f(-.10f, -1.10f, 1.0f);
    glVertex3f(-.10f, -1.10f, 0.9f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(-.20f, 1.10f, 0.9f);
    glVertex3f(-.20f, 1.10f, 1.0f);
    glVertex3f(-.20f, -1.10f, 1.0f);
    glVertex3f(-.20f, -1.10f, 0.9f);




    //  2nd right

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.20f, 1.10f, 1.2f);
	glVertex3f(.10f, 1.10f, 1.2f);
	glVertex3f(.10f, -1.10f, 1.2f);
    glVertex3f(.20f, -1.10f, 1.2f);

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.20f, 1.10f, 1.1f);
	glVertex3f(.10f, 1.10f, 1.1f);
	glVertex3f(.10f, -1.10f, 1.1f);
    glVertex3f(.20f, -1.10f, 1.1f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.10f, 1.10f, 1.1f);
    glVertex3f(.10f, 1.10f, 1.2f);
    glVertex3f(.10f, -1.10f, 1.2f);
    glVertex3f(.10f, -1.10f, 1.1f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(.20f, 1.10f, 1.1f);
    glVertex3f(.20f, 1.10f, 1.2f);
    glVertex3f(.20f, -1.10f, 1.2f);
    glVertex3f(.20f, -1.10f, 1.1f);



    // 2nd top

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(-.20f, 1.20f, 1.0f);
	glVertex3f(.20f, 1.20f, 1.2f);
	glVertex3f(.20f, 1.10f, 1.2f);
    glVertex3f(-.20f, 1.10f, 1.0f);

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(-.20f, 1.20f, 0.9f);
	glVertex3f(.20f, 1.20f, 1.1f);
	glVertex3f(.20f, 1.10f, 1.1f);
    glVertex3f(-.20f, 1.10f, 0.9f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(-.20f, 1.20f, 0.9f);
	glVertex3f(-.20f, 1.20f, 1.0f);
	glVertex3f(-.20f, 1.10f, 1.0f);
    glVertex3f(-.20f, 1.10f, 0.9f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(.20f, 1.20f, 1.1f);
	glVertex3f(.20f, 1.20f, 1.2f);
	glVertex3f(.20f, 1.10f, 1.2f);
    glVertex3f(.20f, 1.10f, 1.1f);


    // top
    glNormal3f(0.0, -1.0f, 0.0f);
    glVertex3f(-.20f, 1.20f, 0.9f);
	glVertex3f(.20f, 1.20f, 1.1f);
	glVertex3f(.20f, 1.20f, 1.2f);
	glVertex3f(-.20f, 1.20f, 1.0f);


    glEnd();





    // left 4th

    // right big child
    glBegin(GL_QUADS);

    glColor3f(1,1,1);

    // 4th top
    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(1.20f, 1.20f, 1.2f);
	glVertex3f(1.60f, 1.20f, 1.0f);
	glVertex3f(1.60f, 1.10f, 1.0f);
    glVertex3f(1.20f, 1.10f, 1.2f);



    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(1.20f, 1.20f, 1.1f);
	glVertex3f(1.60f, 1.20f, 0.9f);
	glVertex3f(1.60f, 1.10f, 0.9f);
    glVertex3f(1.20f, 1.10f, 1.1f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(1.20f, 1.20f, 1.1f);
	glVertex3f(1.20f, 1.20f, 1.2f);
	glVertex3f(1.20f, 1.10f, 1.2f);
    glVertex3f(1.20f, 1.10f, 1.1f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.60f, 1.20f, 0.9f);
	glVertex3f(1.60f, 1.20f, 1.0f);
	glVertex3f(1.60f, 1.10f, 1.0f);
    glVertex3f(1.60f, 1.10f, 0.9f);


    // top
    glNormal3f(0.0, -1.0f, 0.0f);
    glVertex3f(1.20f, 1.20f, 1.2f);
	glVertex3f(1.60f, 1.20f, 1.0f);
	glVertex3f(1.60f, 1.20f, 0.9f);
	glVertex3f(1.20f, 1.20f, 1.1f);





	// 4th left


    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(1.20f, 1.10f, 1.1f);
	glVertex3f(1.30f, 1.10f, 1.1f);
	glVertex3f(1.30f, -1.10f, 1.1f);
    glVertex3f(1.20f, -1.10f, 1.1f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(1.20f, 1.10f, 1.2f);
	glVertex3f(1.30f, 1.10f, 1.2f);
	glVertex3f(1.30f, -1.10f, 1.2f);
    glVertex3f(1.20f, -1.10f, 1.2f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(1.20f, 1.10f, 1.1f);
    glVertex3f(1.20f, 1.10f, 1.2f);
    glVertex3f(1.20f, -1.10f, 1.2f);
    glVertex3f(1.20f, -1.10f, 1.1f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.30f, 1.10f, 1.1f);
    glVertex3f(1.30f, 1.10f, 1.2f);
    glVertex3f(1.30f, -1.10f, 1.2f);
    glVertex3f(1.30f, -1.10f, 1.1f);

    // 4th right

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(1.60f, 1.10f, 0.9f);
	glVertex3f(1.50f, 1.10f, 0.9f);
	glVertex3f(1.50f, -1.10f, 0.9f);
    glVertex3f(1.60f, -1.10f, 0.9f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(1.60f, 1.10f, 1.0f);
	glVertex3f(1.50f, 1.10f, 1.0f);
	glVertex3f(1.50f, -1.10f, 1.0f);
    glVertex3f(1.60f, -1.10f, 1.0f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(1.50f, 1.10f, 0.9f);
    glVertex3f(1.50f, 1.10f, 1.0f);
    glVertex3f(1.50f, -1.10f, 1.0f);
    glVertex3f(1.50f, -1.10f, 0.9f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.60f, 1.10f, 0.9f);
    glVertex3f(1.60f, 1.10f, 1.0f);
    glVertex3f(1.60f, -1.10f, 1.0f);
    glVertex3f(1.60f, -1.10f, 0.9f);


    glEnd();






    // left 1st
    /// little child
    glBegin(GL_QUADS);

    glColor3f(1,1,1);



    // 1st left

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(-.80f, .50f, 0.8f);
	glVertex3f(-.70f, .50f, 0.8f);
	glVertex3f(-.70f, -1.10f, 0.8f);
    glVertex3f(-.80f, -1.10f, 0.8f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(-.80f, .50f, 0.9f);
	glVertex3f(-.70f, .50f, 0.9f);
	glVertex3f(-.70f, -1.10f, 0.9f);
    glVertex3f(-.80f, -1.10f, 0.9f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(-.70f, .50f, 0.8f);
    glVertex3f(-.70f, .50f, 0.9f);
    glVertex3f(-.70f, -1.10f, 0.9f);
    glVertex3f(-.70f, -1.10f, 0.8f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(-.80f, .50f, 0.8f);
    glVertex3f(-.80f, .50f, 0.9f);
    glVertex3f(-.80f, -1.10f, 0.9f);
    glVertex3f(-.80f, -1.10f, 0.8f);


    // 1st right

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(-.40f, .50f, 1.0f);
	glVertex3f(-.50f, .50f, 1.0f);
	glVertex3f(-.50f, -1.10f, 1.0f);
    glVertex3f(-.40f, -1.10f, 1.0f);


    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(-.40f, .50f, 1.1f);
	glVertex3f(-.50f, .50f, 1.1f);
	glVertex3f(-.50f, -1.10f, 1.1f);
    glVertex3f(-.40f, -1.10f, 1.1f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(-.50f, .50f, 1.0f);
    glVertex3f(-.50f, .50f, 1.1f);
    glVertex3f(-.50f, -1.10f, 1.1f);
    glVertex3f(-.50f, -1.10f, 1.0f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(-.40f, .50f, 1.0f);
    glVertex3f(-.40f, .50f, 1.1f);
    glVertex3f(-.40f, -1.10f, 1.1f);
    glVertex3f(-.40f, -1.10f, 1.0f);


    // 1st top

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(-.80f, .60f, 0.9f);
	glVertex3f(-.40f, .60f, 1.1f);
	glVertex3f(-.40f, .50f, 1.1f);
    glVertex3f(-.80f, .50f, 0.9f);

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(-.80f, .60f, 0.8f);
	glVertex3f(-.40f, .60f, 1.0f);
	glVertex3f(-.40f, .50f, 1.0f);
    glVertex3f(-.80f, .50f, 0.8f);


    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(-.80f, .50f, 0.8f);
	glVertex3f(-.80f, .60f, 0.8f);
	glVertex3f(-.80f, .60f, 0.9f);
	glVertex3f(-.80f, .50f, 0.9f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(-.40f, .50f, 1.0f);
	glVertex3f(-.40f, .60f, 1.0f);
	glVertex3f(-.40f, .60f, 1.1f);
	glVertex3f(-.40f, .50f, 1.1f);

	// top
    glNormal3f(0.0, -1.0f, 0.0f);
	glVertex3f(-.80f, .60f, 0.8f);
	glVertex3f(-.40f, .60f, 1.0f);
	glVertex3f(-.40f, .60f, 1.1f);
	glVertex3f(-.80f, .60f, 0.9f);


    glEnd();


    //left 5th



    glBegin(GL_QUADS);

    glColor3f(1,1,1);


    // 5th left


    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(1.70f, .50f, 1.0f);
	glVertex3f(1.80f, .50f, 1.0f);
	glVertex3f(1.80f, -1.10f, 1.0f);
    glVertex3f(1.70f, -1.10f, 1.0f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(1.70f, .50f, 1.1f);
	glVertex3f(1.80f, .50f, 1.1f);
	glVertex3f(1.80f, -1.10f, 1.1f);
    glVertex3f(1.70f, -1.10f, 1.1f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(1.70f, .50f, 1.0f);
    glVertex3f(1.70f, .50f, 1.1f);
    glVertex3f(1.70f, -1.10f, 1.1f);
    glVertex3f(1.70f, -1.10f, 1.0f);


    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.80f, .50f, 1.0f);
    glVertex3f(1.80f, .50f, 1.1f);
    glVertex3f(1.80f, -1.10f, 1.1f);
    glVertex3f(1.80f, -1.10f, 1.0f);




    // 5th right
    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(2.10f, .50f, 0.8f);
	glVertex3f(2.00f, .50f, 0.8f);
	glVertex3f(2.00f, -1.10f, 0.8f);
    glVertex3f(2.10f, -1.10f, 0.8f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(2.10f, .50f, 0.9f);
	glVertex3f(2.00f, .50f, 0.9f);
	glVertex3f(2.00f, -1.10f, 0.9f);
    glVertex3f(2.10f, -1.10f, 0.9f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(2.00f, .50f, 0.8f);
    glVertex3f(2.00f, .50f, 0.9f);
    glVertex3f(2.00f, -1.10f, 0.9f);
    glVertex3f(2.00f, -1.10f, 0.8f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(2.10f, .50f, 0.8f);
    glVertex3f(2.10f, .50f, 0.9f);
    glVertex3f(2.10f, -1.10f, 0.9f);
    glVertex3f(2.10f, -1.10f, 0.8f);



    // 5th top
    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(1.70f, .60f, 1.0f);
	glVertex3f(2.10f, .60f, 0.8f);
	glVertex3f(2.10f, .50f, 0.8f);
    glVertex3f(1.70f, .50f, 1.0f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(1.70f, .60f, 1.1f);
	glVertex3f(2.10f, .60f, 0.9f);
	glVertex3f(2.10f, .50f, 0.9f);
    glVertex3f(1.70f, .50f, 1.1f);

	// left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(1.7f, .50f, 1.0f);
	glVertex3f(1.7f, .60f, 1.0f);
	glVertex3f(1.7f, .60f, 1.1f);
	glVertex3f(1.7f, .50f, 1.1f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(2.1f, .50f, 0.8f);
	glVertex3f(2.1f, .60f, 0.8f);
	glVertex3f(2.1f, .60f, 0.9f);
	glVertex3f(2.1f, .50f, 0.9f);

	// top
	glNormal3f(0.0, -1.0f, 0.0f);
	glVertex3f(1.70f, .60f, 1.0f);
	glVertex3f(2.10f, .60f, 0.8f);
	glVertex3f(2.10f, .60f, 0.9f);
	glVertex3f(1.70f, .60f, 1.1f);


    glEnd();




    // left 3rd -- center


    /// Mother
    glColor3f(1.0f,1.0f, 1.0f);

    glBegin(GL_QUADS);


    // bottom

    // mother bottom left
    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.35f, 1.20f, 1.0f);
	glVertex3f(.45f, 1.20f, 1.0f);
	glVertex3f(.45f, -1.01f, 1.0f);
    glVertex3f(.35f, -1.01f, 1.0f);


    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.35f, 1.20f, 1.1f);
	glVertex3f(.45f, 1.20f, 1.1f);
	glVertex3f(.45f, -1.01f, 1.1f);
    glVertex3f(.35f, -1.01f, 1.1f);

    //left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.35f, 1.20f, 1.0f);
    glVertex3f(.35f, 1.20f, 1.1f);
    glVertex3f(.35f, -1.01f, 1.1f);
    glVertex3f(.35f, -1.01f, 1.0f);

    //right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(.45f, 1.20f, 1.0f);
    glVertex3f(.45f, 1.20f, 1.1f);
    glVertex3f(.45f, -1.01f, 1.1f);
    glVertex3f(.45f, -1.01f, 1.0f);

    // end mother bottom left






    // mother bottom center

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.75f, 1.20f, 1.1f);
	glVertex3f(.65f, 1.20f, 1.1f);
	glVertex3f(.65f, -1.01f, 1.1f);
    glVertex3f(.75f, -1.01f, 1.1f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(.75f, 1.20f, 1.0f);
    glVertex3f(.75f, 1.20f, 1.1f);
    glVertex3f(.75f, -1.01f, 1.1f);
    glVertex3f(.75f, -1.01f, 1.0f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.65f, 1.20f, 1.0f);
    glVertex3f(.65f, 1.20f, 1.1f);
    glVertex3f(.65f, -1.01f, 1.1f);
    glVertex3f(.65f, -1.01f, 1.0f);

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.75f, 1.20f, 1.0f);
	glVertex3f(.65f, 1.20f, 1.0f);
	glVertex3f(.65f, -1.01f, 1.0f);
    glVertex3f(.75f, -1.01f, 1.0f);

    // end mother bottom center




    // mother bottom right

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(1.05f, 1.20f, 1.0f);
	glVertex3f(.95f, 1.20f, 1.0f);
	glVertex3f(.95f, -1.01f, 1.0f);
    glVertex3f(1.05f, -1.01f, 1.0f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(1.05f, 1.20f, 1.1f);
	glVertex3f(.95f, 1.20f, 1.1f);
	glVertex3f(.95f, -1.01f, 1.1f);
    glVertex3f(1.05f, -1.01f, 1.1f);


    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(0.95f, 1.20f, 1.0f);
    glVertex3f(0.95f, 1.20f, 1.1f);
    glVertex3f(0.95f, -1.01f, 1.1f);
    glVertex3f(0.95f, -1.01f, 1.0f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.05f, 1.20f, 1.0f);
    glVertex3f(1.05f, 1.20f, 1.1f);
    glVertex3f(1.05f, -1.01f, 1.1f);
    glVertex3f(1.05f, -1.01f, 1.0f);

    // end mother bottom right






    // mother top


    // mother top left

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.35f, 1.20f, 1.0f);
	glVertex3f(.45f, 1.20f, 1.0f);
	glVertex3f(.45f, 1.60f, 0.6f);
    glVertex3f(.35f, 1.60f, 0.6f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.35f, 1.20f, 1.1f);
	glVertex3f(.45f, 1.20f, 1.1f);
	glVertex3f(.45f, 1.60f, 0.7f);
    glVertex3f(.35f, 1.60f, 0.7f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.35f, 1.20f, 1.0f);
    glVertex3f(.35f, 1.20f, 1.1f);
    glVertex3f(.35f, 1.60f, 0.7f);
    glVertex3f(.35f, 1.60f, 0.6f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(.45f, 1.20f, 1.0f);
    glVertex3f(.45f, 1.20f, 1.1f);
    glVertex3f(.45f, 1.60f, 0.7f);
    glVertex3f(.45f, 1.60f, 0.6f);

    // top
    glNormal3f(1.0, -1.0f, 0.0f);
    glVertex3f(.35f, 1.60f, 0.7f);
    glVertex3f(.45f, 1.60f, 0.7f);
    glVertex3f(.45f, 1.60f, 0.6f);
    glVertex3f(.35f, 1.60f, 0.6f);

    // end mother top left




    // mother top center

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.65f, 1.20f, 1.0f);
	glVertex3f(.75f, 1.20f, 1.0f);
	glVertex3f(.75f, 1.60f, 0.6f);
    glVertex3f(.65f, 1.60f, 0.6f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.65f, 1.20f, 1.1f);
	glVertex3f(.75f, 1.20f, 1.1f);
	glVertex3f(.75f, 1.60f, 0.7f);
    glVertex3f(.65f, 1.60f, 0.7f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.65f, 1.20f, 1.0f);
    glVertex3f(.65f, 1.20f, 1.1f);
    glVertex3f(.65f, 1.60f, 0.7f);
    glVertex3f(.65f, 1.60f, 0.6f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(.75f, 1.20f, 1.0f);
    glVertex3f(.75f, 1.20f, 1.1f);
    glVertex3f(.75f, 1.60f, 0.7f);
    glVertex3f(.75f, 1.60f, 0.6f);

    // top
    glNormal3f(1.0, -1.0f, 0.0f);
    glVertex3f(.65f, 1.60f, 0.7f);
    glVertex3f(.75f, 1.60f, 0.7f);
    glVertex3f(.75f, 1.60f, 0.6f);
    glVertex3f(.65f, 1.60f, 0.6f);

    // mother top center end




    // mother top right

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.95f, 1.20f, 1.0f);
	glVertex3f(1.05f, 1.20f, 1.0f);
	glVertex3f(1.05f, 1.60f, 0.6f);
    glVertex3f(.95f, 1.60f, 0.6f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.95f, 1.20f, 1.1f);
	glVertex3f(1.05f, 1.20f, 1.1f);
	glVertex3f(1.05f, 1.60f, 0.7f);
    glVertex3f(.95f, 1.60f, 0.7f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.95f, 1.20f, 1.0f);
    glVertex3f(.95f, 1.20f, 1.1f);
    glVertex3f(.95f, 1.60f, 0.7f);
    glVertex3f(.95f, 1.60f, 0.6f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.05f, 1.20f, 1.0f);
    glVertex3f(1.05f, 1.20f, 1.1f);
    glVertex3f(1.05f, 1.60f, 0.7f);
    glVertex3f(1.05f, 1.60f, 0.6f);


    // top
    glNormal3f(0.0, -1.0f, 0.0f);
    glVertex3f(.95f, 1.60f, 0.7f);
	glVertex3f(1.05f, 1.60f, 0.7f);
	glVertex3f(1.05f, 1.60f, 0.6f);
    glVertex3f(.95f, 1.60f, 0.76);

    // mother top right end




    // top top

    // back
    glNormal3f(0.0, 0.0f, 1.0f);
    glVertex3f(.35f, 1.60f, 0.6f);
    glVertex3f(1.05f, 1.60f, 0.6f);
    glVertex3f(1.05f, 1.50f, 0.7f);
    glVertex3f(.35f, 1.50f, 0.7f);

    // front
    glNormal3f(0.0, 0.0f, -1.0f);
    glVertex3f(.35f, 1.60f, 0.7f);
    glVertex3f(1.05f, 1.60f, 0.7f);
    glVertex3f(1.05f, 1.50f, 0.8f);
    glVertex3f(.35f, 1.50f, 0.8f);

    // left
    glNormal3f(1.0, 0.0f, 0.0f);
    glVertex3f(.35f, 1.60f, 0.7f);
    glVertex3f(.35f, 1.60f, 0.6);
    glVertex3f(.35f, 1.50f, 0.7f);
    glVertex3f(.35f, 1.50f, 0.8f);

    // right
    glNormal3f(-1.0, 0.0f, 0.0f);
    glVertex3f(1.05f, 1.60f, 0.7f);
    glVertex3f(1.05f, 1.60f, 0.6);
    glVertex3f(1.05f, 1.50f, 0.7f);
    glVertex3f(1.05f, 1.50f, 0.8f);

    // top
    glNormal3f(0.0, -1.0f, 0.0f);
    glVertex3f(.35f, 1.60f, 0.7f);
	glVertex3f(1.05f, 1.60f, 0.7f);
	glVertex3f(1.05f, 1.60f, 0.6f);
    glVertex3f(.35f, 1.60f, 0.76);



    glEnd();


    glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, _textureIdbrick);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


	glPushMatrix();
	// top
	glTranslatef(0.0f, -0.81f, 0.0f);

    glColor3f(1,1,1);

    glBegin(GL_QUADS);


    glNormal3f(0.0, 1.0f, 0.0f);


    glTexCoord2f(0.0f, 0.0f);
    glVertex3f(-3.0f, 0.0f, 4.0f);
    glTexCoord2f(5.0f, 0.0f);
    glVertex3f(4.0f, 0.0f, 4.0f);
    glTexCoord2f(5.0f, 5.0f);
    glVertex3f(4.0f, 0.0f, -2.6f);
    glTexCoord2f(0.0f, 5.0f);
    glVertex3f(-3.0f, 0.0f, -2.6f);

    glEnd();

        // front
        glPushMatrix();
        glTranslatef(0.0f, 0.0f, 4.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(0.0, 0.0f, -1.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-3.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(4.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(4.0f, -0.10f, 0.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(-3.0f, -0.10f, 0.0f);

        glEnd();
        glPopMatrix();

        // back

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, -2.6f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(0.0, 0.0f, 1.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-3.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(4.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(4.0f, -0.10f, 0.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(-3.0f, -0.10f, 0.0f);

        glEnd();
        glPopMatrix();

        // left


        glPushMatrix();
        glTranslatef(-3.0f, 0.0f, 0.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(1.0, 0.0f, 0.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 4.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, -2.6f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, -2.6f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, 4.0f);

        glEnd();
        glPopMatrix();

        // right


        glPushMatrix();
        glTranslatef(4.0f, 0.0f, 0.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(-1.0, 0.0f, 0.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 4.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, -2.6f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, -2.6f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, 4.0f);

        glEnd();
        glPopMatrix();



    glPopMatrix();
    glDisable(GL_TEXTURE_2D);


    // red floor 2
glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, _textureIdbrick);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

	glPushMatrix();
	// top
	glTranslatef(0.0f, -0.91f, 0.0f);

    glColor3f(1,1,1);

    glBegin(GL_QUADS);


    glNormal3f(0.0, 1.0f, 0.0f);


    glTexCoord2f(0.0f, 0.0f);
    glVertex3f(-3.0f, 0.0f, 4.0f);
    glTexCoord2f(5.0f, 0.0f);
    glVertex3f(4.0f, 0.0f, 4.0f);
    glTexCoord2f(5.0f, 20.0f);
    glVertex3f(4.0f, 0.0f, -2.8f);
    glTexCoord2f(0.0f, 5.0f);
    glVertex3f(-3.0f, 0.0f, -2.8f);

    glEnd();

        // front

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, 4.0f);

        glColor3f(1.0,1.0,1.0);

        glBegin(GL_QUADS);


        glNormal3f(0.0, 0.0f, -1.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-3.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(4.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(4.0f, -0.10f, 0.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(-3.0f, -0.10f, 0.0f);

        glEnd();
        glPopMatrix();

        // back

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, -2.8f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(0.0, 0.0f, 1.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-3.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(4.0f, 0.0f, 0.0f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(4.0f, -0.10f, 0.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(-3.0f, -0.10f, 0.0f);

        glEnd();
        glPopMatrix();

        // left


        glPushMatrix();
        glTranslatef(-3.0f, 0.0f, 0.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(1.0, 0.0f, 0.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 4.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, -2.8f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, -2.8f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, 4.0f);

        glEnd();
        glPopMatrix();

        // right


        glPushMatrix();
        glTranslatef(4.0f, 0.0f, 0.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(-1.0, 0.0f, 0.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 4.0f);
        glTexCoord2f(5.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, -2.8f);
        glTexCoord2f(5.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, -2.8f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, 4.0f);

        glEnd();
        glPopMatrix();



    glPopMatrix();
    glDisable(GL_TEXTURE_2D);



    // red floor 3

glEnable(GL_TEXTURE_2D);
    glBindTexture(GL_TEXTURE_2D, _textureIdbrick);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glPushMatrix();
	// top
	glTranslatef(0.0f, -1.01f, 0.0f);

    glColor3f(1,1,1);

    glBegin(GL_QUADS);


    glNormal3f(0.0, 1.0f, 0.0f);


    glTexCoord2f(0.0f, 0.0f);
    glVertex3f(-3.0f, 0.0f, 4.0f);
    glTexCoord2f(20.0f, 0.0f);
    glVertex3f(4.0f, 0.0f, 4.0f);
    glTexCoord2f(20.0f, 20.0f);
    glVertex3f(4.0f, 0.0f, -3.0f);
    glTexCoord2f(0.0f, 20.0f);
    glVertex3f(-3.0f, 0.0f, -3.0f);

    glEnd();

        // front

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, 4.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(0.0, 0.0f, -1.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-3.0f, 0.0f, 0.0f);
        glTexCoord2f(20.0f, 0.0f);
        glVertex3f(4.0f, 0.0f, 0.0f);
        glTexCoord2f(20.0f, 0.5f);
        glVertex3f(4.0f, -0.10f, 0.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(-3.0f, -0.10f, 0.0f);

        glEnd();
        glPopMatrix();

        // back

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, -3.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(0.0, 0.0f, 1.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-3.0f, 0.0f, 0.0f);
        glTexCoord2f(20.0f, 0.0f);
        glVertex3f(4.0f, 0.0f, 0.0f);
        glTexCoord2f(20.0f, 0.5f);
        glVertex3f(4.0f, -0.10f, 0.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(-3.0f, -0.10f, 0.0f);

        glEnd();
        glPopMatrix();

        // left


        glPushMatrix();
        glTranslatef(-3.0f, 0.0f, 0.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(1.0, 0.0f, 0.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 4.0f);
        glTexCoord2f(20.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, -3.0f);
        glTexCoord2f(20.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, -3.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, 4.0f);

        glEnd();
        glPopMatrix();

        // right


        glPushMatrix();
        glTranslatef(4.0f, 0.0f, 0.0f);

        glColor3f(1,1,1);

        glBegin(GL_QUADS);


        glNormal3f(-1.0, 0.0f, 0.0f);


        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, 4.0f);
        glTexCoord2f(20.0f, 0.0f);
        glVertex3f(0.0f, 0.0f, -3.0f);
        glTexCoord2f(20.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, -3.0f);
        glTexCoord2f(0.0f, 0.5f);
        glVertex3f(0.0f, -0.10f, 4.0f);

        glEnd();
        glPopMatrix();



    glPopMatrix();
    glDisable(GL_TEXTURE_2D);

 float x1,y1,x2,y2;
float angle;
glTranslatef (0.2,-0.2,1.2555);
double radius=0.8;
x1 = 0.5,y1=0.6;
glNormal3f(0,0,1);
glColor3f(1.0,0.0,0.0);

glBegin(GL_TRIANGLE_FAN);
glVertex2f(x1,y1);

for (angle=1.0f;angle<361.0f;angle+=0.2)
{
    x2 = x1+sin(angle)*radius;
    y2 = y1+cos(angle)*radius;
    glVertex2f(x2,y2);
}

glEnd();


/*glPushMatrix();
glTranslatef(0,-2,0);

	glBegin(GL_QUADS);

    glColor3f(1,1,1);

    glNormal3f(0.0, 1.0f, 0.0f);
    glVertex3f(-7.0f, 1.0f, 11.0f);
	glVertex3f(7.0f, 1.0f, 11.0f);
	glVertex3f(10.0f, 1.0f, -11.0f);
    glVertex3f(-10.0f, 1.0f, -11.0f);


glEnd();
glPopMatrix();*/
    glutSwapBuffers();
}
void update(int x) {

    if(ghurbe == 1){
            _angle += 1.5f;
            if (_angle > 360) {
                _angle -= 360;
            }

            glutPostRedisplay();
            glutTimerFunc(25, update, 0);
    }
    else if(ghurbe == 0){
            _angle += 0 ;
            if (_angle > 360) {
                _angle -= 360;
            }

            glutPostRedisplay();
            glutTimerFunc(25, update, 0);
    }

}



int main(int argc, char** argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(600, 600);

	glutCreateWindow("Shohid minar");
	initRendering();

	glutDisplayFunc(drawScene);
	glutKeyboardFunc(textureonoff);
	glutReshapeFunc(handleResize);
    glutTimerFunc(25, update, 0);
	glutMainLoop();
	return 0;
}









